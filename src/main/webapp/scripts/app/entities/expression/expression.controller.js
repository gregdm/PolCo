'use strict';

angular.module('polcoApp')
    .controller('ExpressionController', function ($scope, Expression, ExpressionTrans) {
        $scope.expressions = [];
        $scope.expressiontranss = ExpressionTrans.query();
        $scope.loadAll = function() {
            Expression.query(function(result) {
               $scope.expressions = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Expression.update($scope.expression,
                function () {
                    $scope.loadAll();
                    $('#saveExpressionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Expression.get({id: id}, function(result) {
                $scope.expression = result;
                $('#saveExpressionModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Expression.get({id: id}, function(result) {
                $scope.expression = result;
                $('#deleteExpressionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Expression.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExpressionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.expression = {value: null, id: null};
        };
    });
