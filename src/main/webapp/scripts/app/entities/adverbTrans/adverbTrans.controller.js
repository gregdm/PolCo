'use strict';

angular.module('polcoApp')
    .controller('AdverbTransController', function ($scope, AdverbTrans, Adverb) {
        $scope.adverbTranss = [];
        $scope.adverbs = Adverb.query();
        $scope.loadAll = function() {
            AdverbTrans.query(function(result) {
               $scope.adverbTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            AdverbTrans.update($scope.adverbTrans,
                function () {
                    $scope.loadAll();
                    $('#saveAdverbTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            AdverbTrans.get({id: id}, function(result) {
                $scope.adverbTrans = result;
                $('#saveAdverbTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            AdverbTrans.get({id: id}, function(result) {
                $scope.adverbTrans = result;
                $('#deleteAdverbTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AdverbTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAdverbTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.adverbTrans = {value: null, id: null};
        };
    });
