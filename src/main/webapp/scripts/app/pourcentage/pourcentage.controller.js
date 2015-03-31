'use strict';

angular.module('polcoApp')
    .controller('PourcentageController', function ($scope, $translate, $filter, AuditsService, Pourcentage) {

        $scope.text = "greg";
        $scope.pourcentage = "";

        $scope.uploadFile = function(){

            Pourcentage.getPourcentage($scope.text)
                .success(function(data){
                    $scope.pourcentage = data;})
                .error(function(data){
                    $scope.pourcentage = data;
                });
        };

                    $scope.getTextPolCo = function () {
                        $scope.textToTranslate = $scope.textPolCo[Math.floor(Math.random()*$scope.textPolCo.length)];
                    }

                    $scope.getTextNonPolCo = function () {
                        $scope.textToTranslate = $scope.textNonPolCo[Math.floor(Math.random()*$scope.textNonPolCo.length)];
                    }


                    $scope.textPolCo = Array("gregrgeg1", "gregreg2", "grdfsfsdfsf3");
                    $scope.textNonPolCo = Array("gregrgeg", "gregreg", "grdfsfsdfsf");

    });
