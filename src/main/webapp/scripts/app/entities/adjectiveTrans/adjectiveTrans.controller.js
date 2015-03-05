'use strict';

angular.module('polcoApp')
    .controller('AdjectiveTransController', function ($scope, AdjectiveTrans, Adjective) {
        $scope.adjectiveTranss = [];
        $scope.adjectives = Adjective.query();
        $scope.loadAll = function() {
            AdjectiveTrans.query(function(result) {
               $scope.adjectiveTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            AdjectiveTrans.update($scope.adjectiveTrans,
                function () {
                    $scope.loadAll();
                    $('#saveAdjectiveTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            AdjectiveTrans.get({id: id}, function(result) {
                $scope.adjectiveTrans = result;
                $('#saveAdjectiveTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            AdjectiveTrans.get({id: id}, function(result) {
                $scope.adjectiveTrans = result;
                $('#deleteAdjectiveTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AdjectiveTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAdjectiveTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.adjectiveTrans = {value: null, id: null};
        };
    });
