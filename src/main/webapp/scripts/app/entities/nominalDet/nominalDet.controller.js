'use strict';

angular.module('polcoApp')
    .controller('NominalDetController', function ($scope, NominalDet, NominalDetTrans) {
        $scope.nominalDets = [];
        $scope.nominaldettranss = NominalDetTrans.query();
        $scope.loadAll = function() {
            NominalDet.query(function(result) {
               $scope.nominalDets = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            NominalDet.update($scope.nominalDet,
                function () {
                    $scope.loadAll();
                    $('#saveNominalDetModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            NominalDet.get({id: id}, function(result) {
                $scope.nominalDet = result;
                $('#saveNominalDetModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            NominalDet.get({id: id}, function(result) {
                $scope.nominalDet = result;
                $('#deleteNominalDetConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            NominalDet.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNominalDetConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.nominalDet = {value: null, id: null};
        };
    });
