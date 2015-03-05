'use strict';

angular.module('polcoApp')
    .controller('PrepositionTransDetailController', function ($scope, $stateParams, PrepositionTrans, Preposition) {
        $scope.prepositionTrans = {};
        $scope.load = function (id) {
            PrepositionTrans.get({id: id}, function(result) {
              $scope.prepositionTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
