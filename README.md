# 配置

配置类: `name.chengchao.hellosaml.sp.common.CommonConstants`

IDP_METADATA_FILEPATH: meta.xml文件的绝对路径,一般idp都会提供下载



# 测试

- 监听端口: 8088
- 启动类: `name.chengchao.hellosaml.sp.SPDemoApplication`

只需要将hellosaml-idp中的postURL指定成:`http://localhost:8088/sp.do`就能测试了,本质上就是解析SAMLResponse的一个过程

