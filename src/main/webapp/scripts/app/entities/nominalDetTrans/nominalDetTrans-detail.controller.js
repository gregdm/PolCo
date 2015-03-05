'use strict';

angular.module('polcoApp')
    .controller('NominalDetTransDetailController', function ($scope, $stateParams, NominalDetTrans, NominalDet) {
        $scope.nominalDetTrans = {};
        $scope.load = function (id) {
            NominalDetTrans.get({id: id}, function(result) {
              $scope.nominalDetTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
