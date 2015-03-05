'use strict';

angular.module('polcoApp')
    .controller('PrepositionController', function ($scope, Preposition, PrepositionTrans) {
        $scope.prepositions = [];
        $scope.prepositiontranss = PrepositionTrans.query();
        $scope.loadAll = function() {
            Preposition.query(function(result) {
               $scope.prepositions = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Preposition.update($scope.preposition,
                function () {
                    $scope.loadAll();
                    $('#savePrepositionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Preposition.get({id: id}, function(result) {
                $scope.preposition = result;
                $('#savePrepositionModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Preposition.get({id: id}, function(result) {
                $scope.preposition = result;
                $('#deletePrepositionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Preposition.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrepositionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.preposition = {value: null, id: null};
        };
    });
