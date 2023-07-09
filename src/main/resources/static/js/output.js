// 获取文件列表
fetch('/getFileList')
    .then(response => response.json())
    .then(data => {
        const fileList = document.getElementById('fileList');
        data.forEach(file => {
            const listItem = document.createElement('li');
            listItem.textContent = file.name;
            listItem.addEventListener('click', () => {
                fetch('/getFileContent/' + encodeURIComponent(file.name))
                    .then(response => response.text())
                    .then(content => {
                        const codeContent = document.getElementById('fileContentText');
                        codeContent.textContent = "\n" + content;
                    })
                    .catch(error => {
                        console.error('Error fetching file content:', error);
                    });
            });
            fileList.appendChild(listItem);
        });
    })
    .catch(error => {
        console.error('Error fetching file list:', error);
    });

// 复制文件内容到剪贴板
document.addEventListener('DOMContentLoaded', () => {
    const copyButton = document.getElementById('copyButton');
    const downloadButton = document.getElementById('downloadButton');
    const codeContent = document.getElementById('fileContentText');

    copyButton.addEventListener('click', () => {
        const range = document.createRange();
        range.selectNodeContents(codeContent);
        const selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
        document.execCommand('copy');
        selection.removeAllRanges();
        alert('已复制文件内容到剪贴板');
    });

    downloadButton.addEventListener('click', () => {
        // 发起 AJAX 请求
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/downloadZip', true);
        xhr.responseType = 'blob';

        xhr.onload = function () {
            if (xhr.status === 200) {
                // 创建一个临时的下载链接
                var downloadUrl = window.URL.createObjectURL(xhr.response);

                // 创建一个隐藏的 <a> 标签并模拟点击下载链接
                var link = document.createElement('a');
                link.href = downloadUrl;
                link.download = 'output.zip';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);

                // 释放临时下载链接
                window.URL.revokeObjectURL(downloadUrl);
            } else {
                console.error('Error downloading folder:', xhr.status);
            }
        };

        xhr.onerror = function () {
            console.error('Error downloading folder:', xhr.status);
        };

        xhr.send();
    });

});