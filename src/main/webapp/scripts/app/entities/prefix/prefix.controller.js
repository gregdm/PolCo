'use strict';

angular.module('polcoApp')
    .controller('PrefixController', function ($scope, Prefix, PrefixTrans) {
        $scope.prefixs = [];
        $scope.prefixtranss = PrefixTrans.query();
        $scope.loadAll = function() {
            Prefix.query(function(result) {
               $scope.prefixs = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Prefix.update($scope.prefix,
                function () {
                    $scope.loadAll();
                    $('#savePrefixModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Prefix.get({id: id}, function(result) {
                $scope.prefix = result;
                $('#savePrefixModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Prefix.get({id: id}, function(result) {
                $scope.prefix = result;
                $('#deletePrefixConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Prefix.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrefixConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.prefix = {value: null, id: null};
        };
    });
