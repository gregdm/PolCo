'use strict';

angular.module('polcoApp')
    .controller('InterjectionTransController', function ($scope, InterjectionTrans, Interjection) {
        $scope.interjectionTranss = [];
        $scope.interjections = Interjection.query();
        $scope.loadAll = function() {
            InterjectionTrans.query(function(result) {
               $scope.interjectionTranss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            InterjectionTrans.update($scope.interjectionTrans,
                function () {
                    $scope.loadAll();
                    $('#saveInterjectionTransModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            InterjectionTrans.get({id: id}, function(result) {
                $scope.interjectionTrans = result;
                $('#saveInterjectionTransModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            InterjectionTrans.get({id: id}, function(result) {
                $scope.interjectionTrans = result;
                $('#deleteInterjectionTransConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            InterjectionTrans.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteInterjectionTransConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.interjectionTrans = {value: null, id: null};
        };
    });
