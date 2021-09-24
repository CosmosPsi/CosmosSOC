# Cosmos SOC

## 一、项目简介

​      Cosmos-SOC使用Chisel 语言进行硬件敏捷开发，基于 RISC-V RV64 开放指令集的处理器实现。存储系统方面，Cosmos-SOC包含一级指令缓存和数据缓存及二级缓存。 处理器通过系统总线（ AXI、TileLink、APB ）与外界相连. Cosmos-SOC支持 M、S、U 三个特权级, 支持 I、M、A、C以及扩展指令, 包含MMU以支持虚实地址转换, 包含页表缓冲 (TLB) 以加速地址转换过程, 支持 Sv39 分页方案。后续还会为Cosmos内核的部分特性定制专有指令集及SOC模块。

## 二、SOC架构



![Cosmos-SOC](https://github.com/CosmosPsi/CosmosDocs/blob/main/images/RISC-V-SOC.png)



