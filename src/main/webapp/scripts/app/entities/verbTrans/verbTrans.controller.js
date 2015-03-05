'use strict';

angular.module('polcoApp')
    .controller('VerbTransController', function ($scope, VerbTrans, Verb) {
        $scope.verbTranss = [];
        $scope.verbs = Verb.query();
        $scope.loadAll = function() {
            VerbTrans.query(function(result) {
               $scope.verbTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            VerbTrans.update($scope.verbTrans,
                function () {
                    $scope.loadAll();
                    $('#saveVerbTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            VerbTrans.get({id: id}, function(result) {
                $scope.verbTrans = result;
                $('#saveVerbTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            VerbTrans.get({id: id}, function(result) {
                $scope.verbTrans = result;
                $('#deleteVerbTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            VerbTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVerbTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.verbTrans = {value: null, id: null};
        };
    });
