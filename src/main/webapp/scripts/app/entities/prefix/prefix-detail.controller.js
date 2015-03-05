'use strict';

angular.module('polcoApp')
    .controller('PrefixDetailController', function ($scope, $stateParams, Prefix, PrefixTrans) {
        $scope.prefix = {};
        $scope.load = function (id) {
            Prefix.get({id: id}, function(result) {
              $scope.prefix = result;
            });
        };
        $scope.load($stateParams.id);
    });
