'use strict';

angular.module('polcoApp')
    .controller('InterjectionController', function ($scope, Interjection, InterjectionTrans) {
        $scope.interjections = [];
        $scope.interjectiontranss = InterjectionTrans.query();
        $scope.loadAll = function() {
            Interjection.query(function(result) {
               $scope.interjections = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Interjection.update($scope.interjection,
                function () {
                    $scope.loadAll();
                    $('#saveInterjectionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Interjection.get({id: id}, function(result) {
                $scope.interjection = result;
                $('#saveInterjectionModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Interjection.get({id: id}, function(result) {
                $scope.interjection = result;
                $('#deleteInterjectionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Interjection.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteInterjectionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.interjection = {value: null, id: null};
        };
    });
