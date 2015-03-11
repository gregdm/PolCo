'use strict';

angular.module('polcoApp')
    .controller('ExpressionTransController', function ($scope, ExpressionTrans, Expression) {
        $scope.expressionTranss = [];
        $scope.expressions = Expression.query();
        $scope.loadAll = function() {
            ExpressionTrans.query(function(result) {
               $scope.expressionTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            ExpressionTrans.update($scope.expressionTrans,
                function () {
                    $scope.loadAll();
                    $('#saveExpressionTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            ExpressionTrans.get({id: id}, function(result) {
                $scope.expressionTrans = result;
                $('#saveExpressionTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            ExpressionTrans.get({id: id}, function(result) {
                $scope.expressionTrans = result;
                $('#deleteExpressionTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ExpressionTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExpressionTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.expressionTrans = {value: null, id: null};
        };
    });
