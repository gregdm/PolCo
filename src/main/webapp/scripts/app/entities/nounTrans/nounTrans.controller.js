'use strict';

angular.module('polcoApp')
    .controller('NounTransController', function ($scope, NounTrans, Noun) {
        $scope.nounTranss = [];
        $scope.nouns = Noun.query();
        $scope.loadAll = function() {
            NounTrans.query(function(result) {
               $scope.nounTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            NounTrans.update($scope.nounTrans,
                function () {
                    $scope.loadAll();
                    $('#saveNounTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            NounTrans.get({id: id}, function(result) {
                $scope.nounTrans = result;
                $('#saveNounTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            NounTrans.get({id: id}, function(result) {
                $scope.nounTrans = result;
                $('#deleteNounTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            NounTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNounTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.nounTrans = {value: null, id: null};
        };
    });
