'use strict';

angular.module('polcoApp')
    .controller('PrefixTransDetailController', function ($scope, $stateParams, PrefixTrans, Prefix) {
        $scope.prefixTrans = {};
        $scope.load = function (id) {
            PrefixTrans.get({id: id}, function(result) {
              $scope.prefixTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
