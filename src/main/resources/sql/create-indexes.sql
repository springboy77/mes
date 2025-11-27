-- 性能优化索引
CREATE INDEX idx_work_order_status ON work_order(status);
CREATE INDEX idx_work_order_due_date ON work_order(due_date);
CREATE INDEX idx_work_order_priority ON work_order(priority);
CREATE INDEX idx_sales_order_status ON sales_order(status);
CREATE INDEX idx_sales_order_due_date ON sales_order(due_date);
CREATE INDEX idx_operation_routing_id ON operation(routing_id);
CREATE INDEX idx_operation_work_center_id ON operation(work_center_id);
CREATE INDEX idx_bom_parent_item_id ON bill_of_material(parent_item_id);
CREATE INDEX idx_bom_component_item_id ON bill_of_material(component_item_id);
CREATE INDEX idx_routing_item_id ON routing(item_id);
CREATE INDEX idx_item_code ON item(code);
CREATE INDEX idx_work_center_code ON work_center(code);

-- 复合索引
CREATE INDEX idx_work_order_scheduling ON work_order(status, priority, due_date);
CREATE INDEX idx_operation_scheduling ON operation(routing_id, step);