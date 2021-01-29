# reactive rsocket client

[![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://groups.google.com/g/reactive-group)

Reactive RSocket Client Example.

访问Reactive RSocket端口服务的示例程序库

## Contributing

[How to contribute](./CONTRIBUTING.md)

## 启动server监听网络端口

1. 通过`springboot`启动应用程序
2. 通过`shell`命令启动`rsocket`访问程序

1. 通过`test`单元测试进行调试

## 请求网络端口

1. 通过rsocket协议访问`localhost:7000`对应端口，查看数据交互

## 技术原理

### 网络协议抓包分析

1. 启动wireshark，通过适配插件监听本地`localhost`网络包
2. 安装对应协议解析插件，进行网络抓包分析

### Reactor线程模型

通过DEBUG和日志，对线程模型进行分析，查看`Eventloop`网络线程的调用栈