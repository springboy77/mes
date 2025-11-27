package com.cayi.mes.config;

import com.cayi.mes.entity.*;
import com.cayi.mes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private ItemRepository itemRepository;
    @Autowired private WorkCenterRepository workCenterRepository;
    @Autowired private RoutingRepository routingRepository;
    @Autowired private OperationRepository operationRepository;
    @Autowired private BillOfMaterialRepository bomRepository;
    @Autowired private SalesOrderRepository salesOrderRepository;
    @Autowired private WorkOrderRepository workOrderRepository;

    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否是开发环境，生产环境不建议自动初始化测试数据
        boolean isDev = isDevProfile();

        if (isDev && itemRepository.count() == 0) {
            initializeTestData();
        } else if (!isDev) {
            System.out.println("生产环境：跳过测试数据初始化");
        }
    }

    private boolean isDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("dev".equals(profile) || "test".equals(profile)) {
                return true;
            }
        }
        return environment.getActiveProfiles().length == 0; // 默认profile视为开发环境
    }

    private void initializeTestData() {
        System.out.println("初始化测试数据...");

        // 初始化物料数据
        initializeItems();

        // 初始化设备数据
        initializeWorkCenters();

        // 初始化工艺路线和BOM数据
        initializeRoutingAndBOM();

        // 初始化销售订单和工单数据
        initializeSalesAndWorkOrders();

        System.out.println("测试数据初始化完成");
    }

    private void initializeItems() {
        Item rawMaterial1 = new Item("RAW001", "钢材", Item.ItemType.RAW);
        rawMaterial1.setOnHandQty(new BigDecimal("1000.50"));
        rawMaterial1.setDescription("优质钢材");
        itemRepository.save(rawMaterial1);

        Item rawMaterial2 = new Item("RAW002", "塑料粒子", Item.ItemType.RAW);
        rawMaterial2.setOnHandQty(new BigDecimal("500.25"));
        itemRepository.save(rawMaterial2);

        Item semiProduct = new Item("SEMI001", "机加工件", Item.ItemType.SEMI);
        semiProduct.setOnHandQty(new BigDecimal("200.00"));
        itemRepository.save(semiProduct);

        Item finishedProduct = new Item("FIN001", "成品设备", Item.ItemType.FINISHED);
        finishedProduct.setOnHandQty(new BigDecimal("50.00"));
        itemRepository.save(finishedProduct);
    }

    private void initializeWorkCenters() {
        WorkCenter cncMachine = new WorkCenter("WC001", "CNC加工中心");
        cncMachine.setEfficiency(new BigDecimal("0.95"));
        workCenterRepository.save(cncMachine);

        WorkCenter lathe = new WorkCenter("WC002", "数控车床");
        lathe.setEfficiency(new BigDecimal("0.90"));
        workCenterRepository.save(lathe);

        WorkCenter milling = new WorkCenter("WC003", "铣床");
        workCenterRepository.save(milling);

        WorkCenter assembly = new WorkCenter("WC004", "装配线");
        assembly.setEfficiency(new BigDecimal("0.85"));
        workCenterRepository.save(assembly);
    }

    private void initializeRoutingAndBOM() {
        Item finishedProduct = itemRepository.findByCode("FIN001").orElse(null);
        Item semiProduct = itemRepository.findByCode("SEMI001").orElse(null);
        Item steel = itemRepository.findByCode("RAW001").orElse(null);
        Item plastic = itemRepository.findByCode("RAW002").orElse(null);

        if (finishedProduct != null && semiProduct != null && steel != null && plastic != null) {
            // 创建成品设备的工艺路线
            Routing productRouting = new Routing(finishedProduct, "1.0");
            productRouting.setDescription("成品设备生产工艺路线");
            productRouting = routingRepository.save(productRouting);

            // 获取设备
            WorkCenter cnc = workCenterRepository.findByCode("WC001").orElse(null);
            WorkCenter lathe = workCenterRepository.findByCode("WC002").orElse(null);
            WorkCenter assembly = workCenterRepository.findByCode("WC004").orElse(null);

            if (cnc != null && lathe != null && assembly != null) {
                operationRepository.save(new Operation(productRouting, 1, "机加工", cnc, 30));
                operationRepository.save(new Operation(productRouting, 2, "车削加工", lathe, 20));
                operationRepository.save(new Operation(productRouting, 3, "总装配", assembly, 45));
            }

            // 创建BOM
            bomRepository.save(new BillOfMaterial(finishedProduct, semiProduct, new BigDecimal("1.0")));
            bomRepository.save(new BillOfMaterial(finishedProduct, steel, new BigDecimal("2.5")));
            bomRepository.save(new BillOfMaterial(semiProduct, steel, new BigDecimal("1.0")));
            bomRepository.save(new BillOfMaterial(semiProduct, plastic, new BigDecimal("0.5")));
        }
    }

    private void initializeSalesAndWorkOrders() {
        Item finishedProduct = itemRepository.findByCode("FIN001").orElse(null);

        if (finishedProduct != null) {
            // 创建销售订单
            SalesOrder salesOrder1 = new SalesOrder(
                    "SO2024001",
                    finishedProduct,
                    new BigDecimal("10.0"),
                    LocalDateTime.now().plusDays(15)
            );
            salesOrder1.setCustomerName("客户A");
            salesOrder1.setDescription("紧急订单");
            salesOrderRepository.save(salesOrder1);

            SalesOrder salesOrder2 = new SalesOrder(
                    "SO2024002",
                    finishedProduct,
                    new BigDecimal("5.0"),
                    LocalDateTime.now().plusDays(30)
            );
            salesOrder2.setCustomerName("客户B");
            salesOrderRepository.save(salesOrder2);

            // 创建生产工单
            Routing defaultRouting = routingRepository.findByItemIdAndIsDefaultTrue(finishedProduct.getId()).orElse(null);

            WorkOrder workOrder1 = new WorkOrder(
                    "WO20240001",
                    finishedProduct,
                    new BigDecimal("10.0"),
                    LocalDateTime.now().plusDays(15)
            );
            workOrder1.setSalesOrder(salesOrder1);
            workOrder1.setRouting(defaultRouting);
            workOrder1.setPriority(1); // 高优先级
            workOrderRepository.save(workOrder1);

            WorkOrder workOrder2 = new WorkOrder(
                    "WO20240002",
                    finishedProduct,
                    new BigDecimal("5.0"),
                    LocalDateTime.now().plusDays(30)
            );
            workOrder2.setSalesOrder(salesOrder2);
            workOrder2.setRouting(defaultRouting);
            workOrderRepository.save(workOrder2);
        }
    }
}