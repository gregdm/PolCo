'use strict';

angular.module('polcoApp')
    .controller('MainController', function ($scope, Principal, Translation) {

        $scope.textToTranslate = "Je n'aime pas les aveugles depuis que je suis au chomage";
        $scope.translatedText = "";

        $scope.translate = function () {
            $scope.translatedText = $scope.textToTranslate;
            Translation.getTranslation($scope.textToTranslate)
                .success(function(data){
                    $scope.translatedText = data;})
                .error(function(data){
                    $scope.translatedText = "";
                });
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
