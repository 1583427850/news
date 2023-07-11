<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    姓名： ${stu.name}<br>
    年龄： ${stu.age}

<h1>list测试</h1><br>
<table>
    <tr>
        <td>序号</td>
        <td>名字</td>
        <td>年龄</td>
    </tr>
<#--    list使用-->
    <#list stus as stu2>
        <tr>
            <td>${stu2_index+1}</td>
            <td>${stu2.name}</td>
            <td>${stu2.age}</td>
        </tr>
    </#list>
</table>
<#--    <h1>map测试</h1><br>-->
<#--    <table>-->
<#--        <tr>-->
<#--            <td>序号</td>-->
<#--            <td>名字</td>-->
<#--            <td>年龄</td>-->
<#--        </tr>-->
<#--        &lt;#&ndash;    map使用&ndash;&gt;-->
<#--&lt;#&ndash;        ?key可以获取map里面的所有key，然后就可以通过key获取里面的值&ndash;&gt;-->

<#--        <#list stuMap?keys as key>-->
<#--            <tr>-->
<#--                <td>${key_index +1}</td>-->
<#--                <td>${stuMap.key.name}</td>-->
<#--                <td>${stuMap.key.age}</td>-->
<#--            </tr>-->
<#--        </#list>-->
<#--    </table>-->
</body>
</html>