'use strict';

angular.module('polcoApp')
    .controller('NounController', function ($scope, Noun, NounTrans) {
        $scope.nouns = [];
        $scope.nountranss = NounTrans.query();
        $scope.loadAll = function() {
            Noun.query(function(result) {
               $scope.nouns = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Noun.update($scope.noun,
                function () {
                    $scope.loadAll();
                    $('#saveNounModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Noun.get({id: id}, function(result) {
                $scope.noun = result;
                $('#saveNounModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Noun.get({id: id}, function(result) {
                $scope.noun = result;
                $('#deleteNounConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Noun.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNounConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.noun = {value: null, gender: null, number: null, compound: null, id: null};
        };
    });
