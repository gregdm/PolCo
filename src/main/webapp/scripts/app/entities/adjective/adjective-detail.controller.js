'use strict';

angular.module('polcoApp')
    .controller('AdjectiveDetailController', function ($scope, $stateParams, Adjective, AdjectiveTrans) {
        $scope.adjective = {};
        $scope.load = function (id) {
            Adjective.get({id: id}, function(result) {
              $scope.adjective = result;
            });
        };
        $scope.load($stateParams.id);
    });
