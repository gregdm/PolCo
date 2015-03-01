'use strict';

angular.module('polcoApp')
    .controller('BadWordController', function ($scope, BadWord, GoodWord) {
        $scope.badWords = [];
        $scope.goodwords = GoodWord.query();
        $scope.loadAll = function() {
            BadWord.query(function(result) {
               $scope.badWords = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            BadWord.update($scope.badWord,
                function () {
                    $scope.loadAll();
                    $('#saveBadWordModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            BadWord.get({id: id}, function(result) {
                $scope.badWord = result;
                $('#saveBadWordModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            BadWord.get({id: id}, function(result) {
                $scope.badWord = result;
                $('#deleteBadWordConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            BadWord.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBadWordConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.badWord = {value: null, type: null, id: null};
        };
    });
