'use strict';

angular.module('polcoApp')
    .controller('NominalDetDetailController', function ($scope, $stateParams, NominalDet, NominalDetTrans) {
        $scope.nominalDet = {};
        $scope.load = function (id) {
            NominalDet.get({id: id}, function(result) {
              $scope.nominalDet = result;
            });
        };
        $scope.load($stateParams.id);
    });
