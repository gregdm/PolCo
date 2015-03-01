'use strict';

angular.module('polcoApp')
    .controller('GoodWordController', function ($scope, GoodWord, BadWord) {
        $scope.goodWords = [];
        $scope.badwords = BadWord.query();
        $scope.loadAll = function() {
            GoodWord.query(function(result) {
               $scope.goodWords = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            GoodWord.update($scope.goodWord,
                function () {
                    $scope.loadAll();
                    $('#saveGoodWordModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            GoodWord.get({id: id}, function(result) {
                $scope.goodWord = result;
                $('#saveGoodWordModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            GoodWord.get({id: id}, function(result) {
                $scope.goodWord = result;
                $('#deleteGoodWordConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            GoodWord.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteGoodWordConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.goodWord = {value: null, level: null, id: null};
        };
    });
