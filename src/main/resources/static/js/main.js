'use strict';

let singleUploadForm = document.querySelector('#singleUploadForm');
let singleFileUploadInput = document.querySelector('#singleFileUploadInput');
let singleFileUploadError = document.querySelector('#singleFileUploadError');
let singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

let singleSearchForm = document.querySelector('#singleSearchForm');
let singleFileSearchError = document.querySelector('#singleFileSearchError');
let singleFileSearchSuccess = document.querySelector('#singleFileSearchSuccess');


let multipleUploadForm = document.querySelector('#multipleUploadForm');
let multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
let multipleFileUploadInput2 = document.querySelector('#multipleFileUploadInput2');
let multipleFileUploadError = document.querySelector('#multipleFileUploadError');
let multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

const preloader = document.querySelector('.preloader');

function isLoading() {
    preloader.classList.remove('preloader-exited');
    preloader.classList.add('preloader-active');
}

function doneLoading() {
    preloader.classList.add('preloader-exiting');
    preloader.classList.remove(('preloader-active'));

    setTimeout(function () {
        preloader.classList.remove('preloader-exiting');
        preloader.classList.add('preloader-exited');
    }, 2000)
}

function submitSingleSearch(searchQuery, pageNumber) {
    isLoading();
    let formData = new FormData();
    formData.append("searchQuery", searchQuery);
    formData.append("pageNumber", pageNumber);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadSearchQuery");

    xhr.onload = function () {
        console.log(xhr.responseText);
        let response = JSON.parse(xhr.responseText);
        if (xhr.status === 200) {
            singleFileSearchError.style.display = "none";
            singleFileSearchSuccess.innerHTML = "<p>File created Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileSearchSuccess.style.display = "block";
        } else {
            singleFileSearchSuccess.style.display = "none";
            singleFileSearchError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
        doneLoading();
    };

    xhr.onerror = function () {
        doneLoading();
    };

    xhr.onabort = function () {
        doneLoading();
    };
    xhr.send(formData);
}


function uploadSingleFile(file) {
    isLoading();
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
        doneLoading();
    };

    xhr.onerror = function () {
        doneLoading();
    };

    xhr.onabort = function () {
        doneLoading();
    };

    xhr.send(formData);
}

// [us, eu]
function uploadMultipleFiles(files) {
    isLoading();
    let formData = new FormData();
    console.log('Files', files);
    files.forEach(function (region) {
        region.forEach(function (file, index) {
            // console.log(`File ${index}:`, file);
            formData.append('files', file);
        })
    })

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
        doneLoading();
    };

    xhr.onerror = function () {
        doneLoading();
    };

    xhr.onabort = function () {
        doneLoading();
    };
    xhr.send(formData);
}

(function () {
    singleSearchForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const searchQuery = document.getElementById("singleSearchFormInput").value;
        const pageNumber = document.getElementById("pageNumberInput").value;

        if (searchQuery.length === 0) {
            singleFileSearchError.innerHTML = "Please fill out the url field";
            singleFileSearchError.style.display = "block";
        }
        if (pageNumber.length === 0 && !pageNumber.numeric) {
            singleFileSearchError.innerHTML = "Please enter page number fields";
            singleFileSearchError.style.display = "block";
        }

        submitSingleSearch(searchQuery, pageNumber);
    });

    singleUploadForm.addEventListener('submit', function (event) {
        const files = singleFileUploadInput.files;
        event.preventDefault();

        console.log(files)
        if (files.length === 0) {
            singleFileUploadError.innerHTML = "Please select a file";
            singleFileUploadError.style.display = "block";
        }
        uploadSingleFile(files[0]);
    });

    multipleUploadForm.addEventListener('submit', function (event) {
        const usFile = multipleFileUploadInput.files;
        const euFile = multipleFileUploadInput2.files;
        const files = [Array.from(usFile), Array.from(euFile)]
        event.preventDefault();
        if (files.length === 0) {
            multipleFileUploadError.innerHTML = "Please select at least one file";
            multipleFileUploadError.style.display = "block";
        }
        uploadMultipleFiles(files);
    });
    console.log('Script Loaded');
})();
