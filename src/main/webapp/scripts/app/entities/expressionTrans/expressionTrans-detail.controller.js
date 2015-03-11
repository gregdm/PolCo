'use strict';

angular.module('polcoApp')
    .controller('ExpressionTransDetailController', function ($scope, $stateParams, ExpressionTrans, Expression) {
        $scope.expressionTrans = {};
        $scope.load = function (id) {
            ExpressionTrans.get({id: id}, function(result) {
              $scope.expressionTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
