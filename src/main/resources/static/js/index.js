// 第一个表单提交按钮点击事件
function nextStep1() {
    console.log('dbConnectForm');

    // 获取输入的数据库连接信息
    var dbUrl = $("#dbUrl").val();
    var username = $("#username").val();
    var password = $("#password").val();

    // 执行数据库连接逻辑，根据具体需求进行操作
    $.ajax({
        url: "/connectMySQLGetTableNames",
        method: "POST",
        data: {
            dbUrl: dbUrl,
            username: username,
            password: password
        },
        success: function (response) {
            console.log(response);
            if (response.error) {
                alert("数据库连接失败，请检查输入的连接信息。");
                return;
            }

            // 模拟从数据库返回的表名数据
            var tableNames = response.tableNames;

            // 将表名数据添加到下拉框
            $.each(tableNames, function (index, tableName) {
                $("#tableSelect").append($('<option>', {
                    value: tableName,
                    text: tableName
                }));
            });

            //下拉框默认选中第一个，默认赋值第一个实体
            $("#entityName").val(convertToCamelCase($("#tableSelect").val()));

            // 禁用第一个表单的输入框和下拉框
            $("#dbUrl").prop("disabled", true);
            $("#username").prop("disabled", true);
            $("#password").prop("disabled", true);
            // 启用第二个表单的输入框和下拉框
            $("#tableSelect").prop("disabled", false);
            $("#entityName").prop("disabled", false);
            $("#nextStep1").prop("disabled", true);
            $("#nextStep2").prop("disabled", false);
        },
        error: function (xhr, status, error) {
            console.log(error);
            alert("数据库连接失败，请检查输入的连接信息。");
        }
    });


}


// 第二个表单提交按钮点击事件
function nextStep2() {

    // 获取选择的表名
    var selectedTable = $("#tableSelect").val();

    // 获取输入的数据库连接信息
    var dbUrl = $("#dbUrl").val();
    var username = $("#username").val();
    var password = $("#password").val();

    // 模拟从数据库返回的字段数据
    var fieldNames = ["test001", "test002", "test003"]; // 示例数据

    // 执行数据库查询操作，获取所选表的字段信息
    // 执行数据库查询操作，获取所选表的字段信息
    $.ajax({
        url: "/getColumnNameByTable",
        method: "POST",
        data: {
            tableName: selectedTable,
            dbUrl: dbUrl,
            username: username,
            password: password
        },
        success: function (response) {
            console.log(response);
            // 从数据库返回的字段数据
            fieldNames = response.fieldNames;

            // 清空第三个表单的复选框
            $(".field-checkboxes").empty();

            // 将字段名数据添加到复选框
            // $.each(fieldNames, function (index, fieldName) {
            //     $(".field-checkboxes").append('<label><input type="checkbox" name="fields" value="' + fieldName + '">' + fieldName + '</label>');
            // });
            // 创建表格的表头
            var table = $('<table>').addClass('field-table');
            var tableHead = $('<thead>');
            var tableHeadRow = $('<tr>');
            var fieldNameHeader = $('<th>').text('字段');
            var listDisplayHeader = $('<th>').text('列表显示');
            var searchBoxHeader = $('<th>').text('搜索框');
            tableHeadRow.append(fieldNameHeader, listDisplayHeader, searchBoxHeader);
            tableHead.append(tableHeadRow);
            table.append(tableHead);

            // 创建表格的表身
            var tableBody = $('<tbody>');
            $.each(fieldNames, function (index, fieldName) {
                var fieldRow = $('<tr>');
                var fieldNameCell = $('<td>').text(fieldName);

                var listDisplayCell = $('<td>');
                var listCheckbox = $('<input>').attr({
                    type: 'checkbox',
                    name: 'list-display',
                    value: fieldName
                });
                listDisplayCell.append(listCheckbox);

                var searchBoxCell = $('<td>');
                var searchCheckbox = $('<input>').attr({
                    type: 'checkbox',
                    name: 'search-box',
                    value: fieldName
                });
                searchBoxCell.append(searchCheckbox);

                fieldRow.append(fieldNameCell, listDisplayCell, searchBoxCell);
                tableBody.append(fieldRow);
            });

            table.append(tableBody);
            $(".field-checkboxes").append(table);

            // 禁用第二个表单的输入框和下拉框
            $("#tableSelect").prop("disabled", true);
            $("#entityName").prop("disabled", true);
            $("#nextStep2").prop("disabled", true);
            $("#nextStep3").prop("disabled", false);

        },
        error: function (error) {
            console.log(error);
        }
    });
}


$(document).on('change', 'input[name="search-box"]', function () {
    var fieldName = $(this).val();
    var listCheckbox = $('input[name="list-display"][value="' + fieldName + '"]');
    var isSearchBoxChecked = $(this).prop('checked');
    if (isSearchBoxChecked) {
        listCheckbox.prop('checked', true);
        listCheckbox.prop('disabled', true);
    } else {
        listCheckbox.prop('disabled', false);
    }
});

// 第三个表单提交按钮点击事件
function nextStep3() {
    // 获取被选中的列表显示字段
    var selectedListDisplay = [];
    $("input[name='list-display']:checked").each(function () {
        selectedListDisplay.push($(this).val());
    });

    // 获取被选中的搜索框字段
    var selectedSearchBox = [];
    $("input[name='search-box']:checked").each(function () {
        selectedSearchBox.push($(this).val());
    });

    // 输出结果
    console.log("选中的列表显示字段: ", selectedListDisplay);
    console.log("选中的搜索框字段: ", selectedSearchBox);

    // 获取输入的数据库连接信息
    var dbUrl = $("#dbUrl").val();
    var username = $("#username").val();
    var password = $("#password").val();

    // 获取被选中的表格值
    var selectedTable = $("#tableSelect").val();
    var entityName = $("#entityName").val();

    // 构造要发送的数据
    var requestData = {
        selectedListDisplay: selectedListDisplay,
        selectedSearchBox: selectedSearchBox,
        selectedTable: selectedTable,
        entityName: entityName,
        dbUrl: dbUrl,
        username: username,
        password: password
    };

    // 发起Ajax请求
    $.ajax({
        url: "/generate", // 后端控制器的URL
        method: "POST", // 请求方法
        data: JSON.stringify(requestData), // 将数据转换为JSON字符串
        contentType: "application/json", // 请求的Content-Type为JSON
        success: function (response) {
            // 请求成功的处理逻辑
            console.log("请求成功");
            console.log(response);
            if (response.success) {
                // 如果success为true，跳转到指定页面
                window.location.href = "/pages/output.html";
            } else {
                // 如果success为false，弹窗显示错误信息
                alert("请求失败：" + response.error);
            }
        },
        error: function (xhr, status, error) {
            // 请求失败的处理逻辑
            console.log("请求失败");
            console.log(error);
        }
    });

}

$(document).ready(function () {
    $("#tableSelect").prop("disabled", true);
    $("#entityName").prop("disabled", true);
    $("#nextStep1").prop("disabled", false);
    $("#nextStep2").prop("disabled", true);
    $("#nextStep3").prop("disabled", true);
    // 当表格选择框的值发生变化时，自动给实体名称文本框赋值
    $("#tableSelect").change(function () {
        var selectedTable = $(this).val();
        var entityName = convertToCamelCase(selectedTable);
        $("#entityName").val(entityName);
    });
});

// 将字符串转换为驼峰命名格式
function convertToCamelCase(text) {
    var words = text.toLowerCase().replace(/[^a-zA-Z0-9]+(.)/g, function (match, chr) {
        return chr.toUpperCase();
    });
    words = words.charAt(0).toUpperCase() + words.slice(1);
    return words;
}