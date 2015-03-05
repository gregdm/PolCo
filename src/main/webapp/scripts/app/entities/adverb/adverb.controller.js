'use strict';

angular.module('polcoApp')
    .controller('AdverbController', function ($scope, Adverb, AdverbTrans) {
        $scope.adverbs = [];
        $scope.adverbtranss = AdverbTrans.query();
        $scope.loadAll = function() {
            Adverb.query(function(result) {
               $scope.adverbs = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Adverb.update($scope.adverb,
                function () {
                    $scope.loadAll();
                    $('#saveAdverbModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Adverb.get({id: id}, function(result) {
                $scope.adverb = result;
                $('#saveAdverbModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Adverb.get({id: id}, function(result) {
                $scope.adverb = result;
                $('#deleteAdverbConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Adverb.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAdverbConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.adverb = {value: null, id: null};
        };
    });
