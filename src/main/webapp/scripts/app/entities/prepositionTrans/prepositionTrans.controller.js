'use strict';

angular.module('polcoApp')
    .controller('PrepositionTransController', function ($scope, PrepositionTrans, Preposition) {
        $scope.prepositionTranss = [];
        $scope.prepositions = Preposition.query();
        $scope.loadAll = function() {
            PrepositionTrans.query(function(result) {
               $scope.prepositionTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            PrepositionTrans.update($scope.prepositionTrans,
                function () {
                    $scope.loadAll();
                    $('#savePrepositionTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            PrepositionTrans.get({id: id}, function(result) {
                $scope.prepositionTrans = result;
                $('#savePrepositionTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            PrepositionTrans.get({id: id}, function(result) {
                $scope.prepositionTrans = result;
                $('#deletePrepositionTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PrepositionTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrepositionTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.prepositionTrans = {value: null, id: null};
        };
    });
