添加一个新的套装需要做的事：
- 代码部分：
  - 效果：
    - 在content.effects.<...>中创建套装效果类（SetEffect）
    - 用LinearFuncHandle做为数值调整接口
    - 写明效果文本并确定数字格式
  - 注册：在init.registrate.ArtifactItemRegistry中注册套装效果和套装：
    - 定义用 public static final，代码写在static{}里面
    - 注册LinearFuncHanler并提供默认数值
    - 创建并注册套装效果，同时提供效果描述文本和名字
    - 注册套装，并指定星级范围和可用套装部位，以及套装名。指定套装效果的套装数量要求，调用regItems()自动注册套装物品
- 资源部分：
  - 资源文件：
    - 添加套装物品贴图，路径为：main/resources/assets/l2artifacts/textures/item/<套装名>/<部位名>.png
    - 运行runData以生成配置文件和物品模型文件，以及自动生成的合成表
  - 中文翻译：
    - 在test/resources/l2artifacts/lang/zh_cn/item.json语言文件中提供套装物品中文名
    - 在test/resources/l2artifacts/lang/zh_cn/regs.json语言文件中提供套装效果中文名和套装效果中文描述
    - 运行ResourceOrganizer以整理并生成中文翻译文件
- 测试：
  - 运行runClient打开游戏测试套装效果

TODO Add instructions on all pages