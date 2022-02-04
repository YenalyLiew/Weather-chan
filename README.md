# Weather-chan

## 前言
一款勉强能看，能用的天气app，全部采用Kotlin和MVVM架构进行编写。

## 功能
Weather-chan的功能有：
1. 查询天气，IP定位本地城市。
2. 添加和删除自己喜欢的城市。
3. 显示天气资讯，如穿衣指数，紫外线指数等。
4. 支持夜间模式。

## 最低配置要求
Android 5.0

## 第三方库使用
`Glide`, `Jetpack`, `Retrofit`, `Gson`.

在okhttp的branch里未使用`Retrofit`而是改用了`OkHttp`，单纯为了考核修改，效果不一定好。

## API使用
彩云天气API（获取实时、未来天气）、
高德地图API（获取IP对应地区）、
IP-API（获取IP，为http连接，不安全）。

暂时未使用SDK，预计马上换，毕竟IP不确定性太大。 

## 缺陷
1. 没有优化View，在主页面滑动ViewPager2时有卡顿。
2. 搜索城市功能简陋，没有采用更佳的处理方式。
3. IP定位易受VPN干扰且不精确，后期应换成GPS定位。
4. 没有使用自定义View，界面稍显生硬。
5. 没有加入间隔时间自动刷新。
6. 不能切换计量单位，语言单一，没有常用设置功能。

## 后续可能加入
1. 未来天气详细情况界面。
2. GPS定位当前位置。
3. 添加设置功能，有更多可选选项。
4. 优化界面。
5. 添加城市Fragment可以更换顺序。

## 参考
部分界面与图片参考于[SunnyWeather](https://github.com/guolindev/SunnyWeather)

## License
```
Copyright (C) Weather-chan Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0
     
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
