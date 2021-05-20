ALTER TABLE `global_library`.`request`
    ADD COLUMN `status` VARCHAR(10) NULL AFTER `date`;

ALTER TABLE `global_library`.`extradition`
    ADD COLUMN `status` VARCHAR(10) NULL AFTER `date`;