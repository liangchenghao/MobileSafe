# MobileSafe
##包括大部分的Android基础知识
启动展示欢迎界面的AlphaAnimation动画，通过Xutils框架的HttpUtils连接本机的服务器，解析JSON文件获取版本号检测更新、下载。
主界面使用GridView展示功能选项。
**手机防盗：**首次启动需要设置MD5密码，进入引导设置界面可左右滑动切换上下步设置，通过overridePendingTransition设置自定义转场动画。使用SharedPreferences同步数据，设置返回键到达主界面。
**高级工具：**电话归属地查询是通过EditText监听输入框中的号码，查询数据库，若未输入点击查询，输入框及手机有抖动效果；短信备份，获取所有短信信息解析生成XML文件保存到sd卡中；程序锁，通过PackageManager获取安装应用信息，使用SQLite数据库保存已上锁应用名，ListView展示应用信。
**设置中心：**通过自定义属性实时更改副标题内容，模仿Toast源码显示来电归属地。
