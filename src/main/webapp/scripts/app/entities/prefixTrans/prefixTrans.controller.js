'use strict';

angular.module('polcoApp')
    .controller('PrefixTransController', function ($scope, PrefixTrans, Prefix) {
        $scope.prefixTranss = [];
        $scope.prefixs = Prefix.query();
        $scope.loadAll = function() {
            PrefixTrans.query(function(result) {
               $scope.prefixTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            PrefixTrans.update($scope.prefixTrans,
                function () {
                    $scope.loadAll();
                    $('#savePrefixTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            PrefixTrans.get({id: id}, function(result) {
                $scope.prefixTrans = result;
                $('#savePrefixTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            PrefixTrans.get({id: id}, function(result) {
                $scope.prefixTrans = result;
                $('#deletePrefixTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PrefixTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrefixTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.prefixTrans = {value: null, id: null};
        };
    });
