'use strict';

angular.module('polcoApp')
    .controller('VerbTradController', function ($scope, VerbTrad, Verb) {
        $scope.verbTrads = [];
        $scope.verbs = Verb.query();
        $scope.loadAll = function() {
            VerbTrad.query(function(result) {
               $scope.verbTrads = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            VerbTrad.update($scope.verbTrad,
                function () {
                    $scope.loadAll();
                    $('#saveVerbTradModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            VerbTrad.get({id: id}, function(result) {
                $scope.verbTrad = result;
                $('#saveVerbTradModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            VerbTrad.get({id: id}, function(result) {
                $scope.verbTrad = result;
                $('#deleteVerbTradConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            VerbTrad.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVerbTradConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.verbTrad = {value: null, id: null};
        };
    });
