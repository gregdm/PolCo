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


    });
