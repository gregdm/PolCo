'use strict';

angular.module('polcoApp')
    .controller('MainController', function ($scope, Principal, Translation) {

        $scope.textToTranslate = "init to text ";
        $scope.translatedText = "soon";

        $scope.uploadFile = function(){
            var file = $scope.myFile;
            console.log('file is ' + JSON.stringify(file));
            var uploadUrl = "/fileUpload";
            Translation.uploadFileToUrl(file, uploadUrl)
                .success(function(data){
                console.log("Test" + data);
                $scope.translatedText = data.value;})
                .error(function(data){
                    $scope.translatedText = data;
                });
        };

        $scope.translate = function () {
            $scope.translatedText = $scope.textToTranslate;
            console.log("Test init");
            debugger;
            Translation.getTranslation($scope.textToTranslate)
                .success(function(data){
                    console.log("Test" + data);
                    $scope.translatedText = data.value;})
                .error(function(data){
                    $scope.translatedText = data;
                });
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
