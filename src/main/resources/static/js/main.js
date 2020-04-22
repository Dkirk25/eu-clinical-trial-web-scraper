'use strict';

let singleUploadForm = document.querySelector('#singleUploadForm');
let singleFileUploadInput = document.querySelector('#singleFileUploadInput');
let singleFileUploadError = document.querySelector('#singleFileUploadError');
let singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

let singleSearchForm = document.querySelector('#singleSearchForm');
let singleSearchFormInput = document.getElementById("singleSearchFormInput").value;
let pageNumberInput = document.querySelector('#pageNumberInput');

let multipleUploadForm = document.querySelector('#multipleUploadForm');
let multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
let multipleFileUploadError = document.querySelector('#multipleFileUploadError');
let multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

function submitSingleSearch(searchQuery, pageNumber) {
    let formData = new FormData();
    formData.append("searchQuery", searchQuery);
    formData.append("pageNumber", pageNumber);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadSearchQuery");

    xhr.onload = function () {
        console.log(xhr.responseText);
        let response = JSON.parse(xhr.responseText);
        if (xhr.status === 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File created Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    };
    xhr.send(formData);
}


function uploadSingleFile(file) {
    let formData = new FormData();
    formData.append("file", file);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadFile");

    xhr.onload = function () {
        console.log(xhr.responseText);
        let response = JSON.parse(xhr.responseText);
        if (xhr.status === 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    };

    xhr.send(formData);
}

function uploadMultipleFiles(files) {
    let formData = new FormData();
    for (let index = 0; index < files.length; index++) {
        formData.append("files", files[index]);
    }

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadMultipleFiles");

    xhr.onload = function () {
        console.log(xhr.responseText);
        let response = JSON.parse(xhr.responseText);
        if (xhr.status === 200) {
            multipleFileUploadError.style.display = "none";
            let content = "<p>All Files Uploaded Successfully</p>";
            for (let i = 0; i < response.length; i++) {
                content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
            }
            multipleFileUploadSuccess.innerHTML = content;
            multipleFileUploadSuccess.style.display = "block";
        } else {
            multipleFileUploadSuccess.style.display = "none";
            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    };
    xhr.send(formData);
}

(function () {
    // window.addEventListener('load', function () {
    //     var element = document.getElementById("singleFileUploadSuccess").value;
    //
    //     var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
    //     var observer = new MutationObserver(myFunction);
    //     observer.observe(element, {
    //         childList: true
    //     });
    //
    //     function myFunction() {
    //         console.log(element);
    //         if(element === ""){
    //             console.log("please load");
    //         }else{
    //             console.log("DO NOT LOAD")
    //         }
    //     }
    // });

    singleSearchForm.addEventListener('submit', function (event) {
        let searchQuery = document.getElementById("singleSearchFormInput").value;
        let pageNumber = document.getElementById("pageNumberInput").value;

        if (searchQuery.length === 0) {
            singleFileUploadError.innerHTML = "Please fill out the url field";
            singleFileUploadError.style.display = "block";
        }
        if(pageNumber.length === 0 && !pageNumber.numeric){
            singleFileUploadError.innerHTML = "Please enter page number fields";
            singleFileUploadError.style.display = "block";
        }

        submitSingleSearch(searchQuery, pageNumber);
        event.preventDefault();
    });


    multipleUploadForm.addEventListener('submit', function (event) {
        let files = multipleFileUploadInput.files;
        if (files.length === 0) {
            multipleFileUploadError.innerHTML = "Please select at least one file";
            multipleFileUploadError.style.display = "block";
        }
        uploadMultipleFiles(files);
        event.preventDefault();
    });

    console.log('Script Loaded');
})();


// singleUploadForm.addEventListener('submit', function (event) {
//     let files = singleFileUploadInput.files;
//     if (files.length === 0) {
//         singleFileUploadError.innerHTML = "Please select a file";
//         singleFileUploadError.style.display = "block";
//     }
//     uploadSingleFile(files[0]);
//     event.preventDefault();
// }, true);
