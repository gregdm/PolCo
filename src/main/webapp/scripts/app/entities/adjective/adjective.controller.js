'use strict';

angular.module('polcoApp')
    .controller('AdjectiveController', function ($scope, Adjective, AdjectiveTrans) {
        $scope.adjectives = [];
        $scope.adjectivetranss = AdjectiveTrans.query();
        $scope.loadAll = function() {
            Adjective.query(function(result) {
               $scope.adjectives = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Adjective.update($scope.adjective,
                function () {
                    $scope.loadAll();
                    $('#saveAdjectiveModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Adjective.get({id: id}, function(result) {
                $scope.adjective = result;
                $('#saveAdjectiveModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Adjective.get({id: id}, function(result) {
                $scope.adjective = result;
                $('#deleteAdjectiveConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Adjective.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAdjectiveConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.adjective = {value: null, gender: null, number: null, id: null};
        };
    });
