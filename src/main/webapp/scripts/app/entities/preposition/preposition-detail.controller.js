'use strict';

angular.module('polcoApp')
    .controller('PrepositionDetailController', function ($scope, $stateParams, Preposition, PrepositionTrans) {
        $scope.preposition = {};
        $scope.load = function (id) {
            Preposition.get({id: id}, function(result) {
              $scope.preposition = result;
            });
        };
        $scope.load($stateParams.id);
    });
