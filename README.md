# reactive web

[![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://groups.google.com/g/reactive-group)

Reactive Web Example.

提供Reactive网络端口服务的示例程序库

## Contributing

[How to contribute](./CONTRIBUTING.md)

## 示例清单

[springboot-webflux-example](./springboot-webflux-example)

[springboot-rsocket-example](./springboot-rsocket-example)

[springboot-rsocket-client-example](./springboot-rsocket-client-example)

[springboot-websocket-example](./springboot-websocket-example)

## 启动server监听网络端口

1. 通过`springboot`启动应用程序

1. 通过`test`单元测试进行调试

## 请求网络端口

1. 通过对应协议访问`localhost`对应端口，查看数据交互

## 技术原理

### 网络协议抓包分析

1. 启动wireshark，通过适配插件监听本地`localhost`网络包
2. 安装对应协议解析插件，进行网络抓包分析

### Reactor线程模型

通过DEBUG和日志，对线程模型进行分析，查看`Eventloop`网络线程的调用栈