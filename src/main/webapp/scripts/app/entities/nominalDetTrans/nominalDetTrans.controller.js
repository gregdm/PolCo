'use strict';

angular.module('polcoApp')
    .controller('NominalDetTransController', function ($scope, NominalDetTrans, NominalDet) {
        $scope.nominalDetTranss = [];
        $scope.nominaldets = NominalDet.query();
        $scope.loadAll = function() {
            NominalDetTrans.query(function(result) {
               $scope.nominalDetTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            NominalDetTrans.update($scope.nominalDetTrans,
                function () {
                    $scope.loadAll();
                    $('#saveNominalDetTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            NominalDetTrans.get({id: id}, function(result) {
                $scope.nominalDetTrans = result;
                $('#saveNominalDetTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            NominalDetTrans.get({id: id}, function(result) {
                $scope.nominalDetTrans = result;
                $('#deleteNominalDetTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            NominalDetTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNominalDetTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.nominalDetTrans = {value: null, id: null};
        };
    });
