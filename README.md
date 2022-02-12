# Weather-chan

## 前言
一款勉强能看，能用的天气app，全部采用Kotlin和MVVM架构进行编写。

## 功能
Weather-chan的功能有：
1. 查询天气，GPS定位本地城市。
2. 隔2分钟自动刷新当前天气情况。
3. 24小时内每小时的天气情况。
4. 添加和删除自己喜欢的城市。
5. 显示天气资讯，如穿衣指数，紫外线指数等。
6. 支持夜间模式。

## 截图示例
![Screenshot_2022-02-12-20-41-23-067_com yenaly wea](https://user-images.githubusercontent.com/92662107/153711830-b0ed73dd-65dc-4c45-9d22-595bce013af7.jpg)
![Screenshot_2022-02-12-20-41-36-239_com yenaly wea](https://user-images.githubusercontent.com/92662107/153711834-1125192a-2dbd-429b-bcde-6f6c6925e594.jpg)
![Screenshot_2022-02-12-20-41-58-385_com yenaly wea](https://user-images.githubusercontent.com/92662107/153711840-b40f0f59-13e0-4b9a-9f7a-4d800dfd5175.jpg)
![Screenshot_2022-02-12-20-41-28-376_com yenaly wea](https://user-images.githubusercontent.com/92662107/153711847-69279335-a24a-43b1-8373-0848fc0393c4.jpg)
![Screenshot_2022-02-12-20-41-31-928_com yenaly wea](https://user-images.githubusercontent.com/92662107/153711852-0f956d6c-ee0f-4b81-898b-3949b578e0e6.jpg)

## 最低配置要求
Android 5.0

## 第三方库使用
`Glide`, `Jetpack`, `Retrofit`, `Gson`.

在okhttp的branch里未使用`Retrofit`而是改用了`OkHttp`，单纯为了考核修改，效果不一定好。

## API使用
彩云天气API（获取每小时、实时、未来天气）、
高德地图SDK（GPS获取当前地区）、

~~高德地图API（获取IP对应地区）、~~
~~IP-API（获取IP，为http连接，不安全）。~~

~~暂时未使用SDK，预计马上换，毕竟IP不确定性太大。~~

## 缺陷
1. 没有优化View，在主页面滑动ViewPager2时有卡顿，部分滑动冲突未解决。
2. 搜索城市功能简陋，没有采用更佳的处理方式。
3. ~~IP定位易受VPN干扰且不精确，后期应换成GPS定位。~~
4. 没有使用自定义View，界面稍显生硬。
5. ~~没有加入间隔时间自动刷新。~~
6. ~~不能切换计量单位~~，语言单一，没有常用设置功能。
7. 没有加入后台功能。

## 后续可能加入
1. 未来天气详细情况界面。
2. ~~每小时的天气情况。~~
3. ~~GPS定位当前位置。~~
4. ~~添加设置功能~~，有更多可选选项。
5. 优化界面。
6. 添加城市Fragment可以更换顺序。

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
