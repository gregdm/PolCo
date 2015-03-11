'use strict';

angular.module('polcoApp')
    .controller('ExpressionDetailController', function ($scope, $stateParams, Expression, ExpressionTrans) {
        $scope.expression = {};
        $scope.load = function (id) {
            Expression.get({id: id}, function(result) {
              $scope.expression = result;
            });
        };
        $scope.load($stateParams.id);
    });
