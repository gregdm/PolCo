'use strict';

angular.module('polcoApp')
    .controller('AdverbDetailController', function ($scope, $stateParams, Adverb, AdverbTrans) {
        $scope.adverb = {};
        $scope.load = function (id) {
            Adverb.get({id: id}, function(result) {
              $scope.adverb = result;
            });
        };
        $scope.load($stateParams.id);
    });
