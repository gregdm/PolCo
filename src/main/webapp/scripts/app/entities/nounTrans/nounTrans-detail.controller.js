'use strict';

angular.module('polcoApp')
    .controller('NounTransDetailController', function ($scope, $stateParams, NounTrans, Noun) {
        $scope.nounTrans = {};
        $scope.load = function (id) {
            NounTrans.get({id: id}, function(result) {
              $scope.nounTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
