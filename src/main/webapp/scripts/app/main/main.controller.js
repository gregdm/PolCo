'use strict';

angular.module('polcoApp')
    .controller('MainController', function ($scope, Principal, Translation) {

        $scope.textToTranslate = "Andréa Andréa J'aime  Andréa andréa andréa Andréaa andréaa pour toujours";
        $scope.translatedText = "";

        $scope.translate = function () {
            Translation.getTranslation($scope.textToTranslate)
                .success(function(data){
                    $scope.translatedText = data;})
                .error(function(data){
                    $scope.translatedText = "";
                });
        };

        $scope.getCitationDivers = function () {
            $scope.textToTranslate = $scope.citationDivers[Math.floor(Math.random()*$scope.citationDivers.length)];
        }

        $scope.getCitationSexiste = function () {
            $scope.textToTranslate = $scope.citationSexiste[Math.floor(Math.random()*$scope.citationSexiste.length)];
        }
        $scope.getCitationRaciste = function () {
            $scope.textToTranslate = $scope.citationRaciste[Math.floor(Math.random()*$scope.citationRaciste.length)];
        }


        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

       $scope.citationDivers = Array("gregrgeg1", "gregreg2", "grdfsfsdfsf3");
       $scope.citationSexiste = Array("gregrgeg", "gregreg", "grdfsfsdfsf");
       $scope.citationRaciste = Array("gregrgeg", "gregreg", "grdfsfsdfsf");
    });
