'use strict';

angular.module('polcoApp')
    .controller('AdjectiveTransDetailController', function ($scope, $stateParams, AdjectiveTrans, Adjective) {
        $scope.adjectiveTrans = {};
        $scope.load = function (id) {
            AdjectiveTrans.get({id: id}, function(result) {
              $scope.adjectiveTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
