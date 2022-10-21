# @requestparam map 接收前端的值_Spring MVC中@RequestParam注解的使用指南



![8713e725a786befbfa0c8cd6f9a6415a.png](.assets/8713e725a786befbfa0c8cd6f9a6415a.png)

# **概述**

学java的同学们大家好，在这个教程中，码农小胖哥将带大家来研究一下Spring的@RequestParam注解。简而言之，我们可以使用@RequestParam从请求中提取查询参数，[表单](https://so.csdn.net/so/search?q=表单&spm=1001.2101.3001.7020)参数甚至文件。我们将讨论如何使用@RequestParam及其属性。我们还将讨论@RequestParam和@PathVariable之间的区别。

简单映射

假设我们有一个端点/ [api](https://so.csdn.net/so/search?q=api&spm=1001.2101.3001.7020) / foos，它接受一个名为id的查询参数：

![813d8b668a879f76c90e114216478d5d.png](.assets/813d8b668a879f76c90e114216478d5d.png)

在此示例中，我们使用 @RequestParam来提取id查询参数。一个简单的GET请求将调用getFoos：

![de14d0d6e1c176a56ce2762d0659a09a.png](.assets/de14d0d6e1c176a56ce2762d0659a09a.png)

接下来，让我们看一下注释的属性：**name， value，required和defaultValue**。

# **指定请求参数名称**

在前面的示例中，变量名称和参数名称都相同。但有时我们希望这些不同。或者，如果我们不使用Spring Boot，我们可能需要进行特殊的编译时配置，否则参数名称实际上不会在编译后的字节码中。**为此我们可以通过name属性配置 @RequestParam名称**：

![9596c3aad6081e2a671a0a9de31367fd.png](.assets/9596c3aad6081e2a671a0a9de31367fd.png)

@RequestParam(value =“id”)等同于 @RequestParam(“id”)。

# **可选的请求参数**

默认情况下，需要使用@RequestParam注释的方法参数 。这意味着如果请求中不存在该参数，我们将收到错误：

![2f0e2078d7dac88aa5b188f5e01738ed.png](.assets/2f0e2078d7dac88aa5b188f5e01738ed.png)

我们可以将@RequestParam的required设置为false ，默认为true(必选)：

![0b81aa0e884dbc3448fb2bd92d7e17a6.png](.assets/0b81aa0e884dbc3448fb2bd92d7e17a6.png)

我们测试带参数和不带参数的情况：

![b7fbab3ce352851875a0a09f541f7996.png](.assets/b7fbab3ce352851875a0a09f541f7996.png)

这样方法如果未指定参数，则将method参数绑定为null，不会出现异常。

# **请求参数的默认值**

我们还可以 使用defaultValue属性为@RequestParam设置默认值：

![51bc3c28b01e4a9f52ed208e0963ee67.png](.assets/51bc3c28b01e4a9f52ed208e0963ee67.png)

**类似required = false， 当不提供参数时注入默认参数**：

![cfc889c1d4150d8b35c914fc6a49275f.png](.assets/cfc889c1d4150d8b35c914fc6a49275f.png)

**当我们提供时，注入提供的参数值：**

![ba1206ba05f1133ad77db9e227da9fce.png](.assets/ba1206ba05f1133ad77db9e227da9fce.png)

**请注意，当我们设置 defaultValue 属性时， required确实设置为false。**

# **映射所有参数**

对于复杂参数同样可以做到映射，前端传入参数以 k=v 形式进行传递，后端将自动注入。以下以Map为例：

![968a09efd96e000ea496cca9dc839b3c.png](.assets/968a09efd96e000ea496cca9dc839b3c.png)

请求结果为：

![b3b4c904b882d4e283f42e8f15f7d6cd.png](.assets/b3b4c904b882d4e283f42e8f15f7d6cd.png)

# 

# **映射多值参数**

单个@RequestParam可以传递多个值比如数组或者结合：

![f6058b8f4355f2fe7acc4e6a08f61e79.png](.assets/f6058b8f4355f2fe7acc4e6a08f61e79.png)

Spring [MVC](https://so.csdn.net/so/search?q=MVC&spm=1001.2101.3001.7020)将映射逗号分隔的 id 参数：

![d2aea4466f8e1e8333ae11105a40ff77.png](.assets/d2aea4466f8e1e8333ae11105a40ff77.png)

或者单独的id参数列表：

![d51dcb58289571e5ccbd25fcc66102f3.png](.assets/d51dcb58289571e5ccbd25fcc66102f3.png)

接下来我们将对比@RequestParam 和 @PathVariable之间的异同。

# **前提概要**

当@RequestParam从查询字符串中提取值时，@ PathVariables从URI路径中提取值：

![da05c507844c0bdc654d1470c2a99397.png](.assets/da05c507844c0bdc654d1470c2a99397.png)

根据路径进行映射出的结果：

![bca8ea9dbff838f2e921b3678bfb6367.png](.assets/bca8ea9dbff838f2e921b3678bfb6367.png)

对于@RequestParam，它将是：

![029c1252ef17f4107fefc9d02268a572.png](.assets/029c1252ef17f4107fefc9d02268a572.png)

跟@ PathVariables相同的响应，只是一个不同的URI：

![6e2fcf786e00d6e89f445a2c46a4d2e8.png](.assets/6e2fcf786e00d6e89f445a2c46a4d2e8.png)

# **URI编码**

RFC3986文档规定，Url中只允许包含英文字母(a-zA-Z)、数字(0-9)、-_.~4个特殊字符以及所有保留字符。US-ASCII字符集中没有对应的可打印字符：Url中只允许使用可打印字符。US-ASCII码中的10-7F字节全都表示控制字符，这些字符都不能直接出现在Url中。同时，对于80-FF字节(ISO-8859-1)，由于已经超出了US-ACII定义的字节范围，因此也不可以放在Url中。

保留字符：Url可以划分成若干个组件，协议、主机、路径等。有一些字符(:/?#[]@)是用作分隔不同组件的。例如：冒号用于分隔协议和主机，/用于分隔主机和路径，?用于分隔路径和查询参数，等等。还有一些字符(!$&’()*+,;=)用于在每个组件中起到分隔作用的，如=用于表示查询参数中的键值对，&符号用于分隔查询多个键值对。当组件中的普通数据包含这些特殊字符时，需要对其进行编码。RFC3986中指定了以下字符为保留字符：! * ’ ( ) ; : @ & = + $ , / ? # [ ]

# **两种注解的编码问题**

我们可以根据上两种注解来分别执行两个示例。

@RequestParam示例结果：

![ec03f7dd9881cd24c6b00a1fba5da66d.png](.assets/ec03f7dd9881cd24c6b00a1fba5da66d.png)

@PathVariable示例结果：

![c54a8dcb2f53b5f3c361d7e2c2c7fbf9.png](.assets/c54a8dcb2f53b5f3c361d7e2c2c7fbf9.png)

根据上面两种结果，@PathVariable 从路径中获取是不用编码解码的，而@RequestParam会进行编码解码。这种特性很少有人注意到，所以我需要特别指出来

# **可选值**

我们可以使用从**Spring 4.3.3**开始的必需属性使@PathVariable成为可选：

![a70ce783f979a000faac42c7e5722cbb.png](.assets/a70ce783f979a000faac42c7e5722cbb.png)

然后我们对该接口可以同时进行下面的操作：

![fa92d410dbd91c0e2ffa99abc5021a90.png](.assets/fa92d410dbd91c0e2ffa99abc5021a90.png)

或者：

![0d15e5559ba5c8c038e1efa61a5adb95.png](.assets/0d15e5559ba5c8c038e1efa61a5adb95.png)

对于@RequestParam，我们也可以通过设置required属性进行实现类似的效果。但是请注意，**在使@PathVariable成为可选时我们应该小心，以避免路径冲突。**