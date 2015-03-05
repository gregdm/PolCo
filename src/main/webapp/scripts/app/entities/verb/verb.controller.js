'use strict';

angular.module('polcoApp')
    .controller('VerbController', function ($scope, Verb, VerbTrans) {
        $scope.verbs = [];
        $scope.verbtranss = VerbTrans.query();
        $scope.loadAll = function() {
            Verb.query(function(result) {
               $scope.verbs = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Verb.update($scope.verb,
                function () {
                    $scope.loadAll();
                    $('#saveVerbModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Verb.get({id: id}, function(result) {
                $scope.verb = result;
                $('#saveVerbModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Verb.get({id: id}, function(result) {
                $scope.verb = result;
                $('#deleteVerbConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Verb.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVerbConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.verb = {value: null, tense: null, person: null, number: null, id: null};
        };
    });
