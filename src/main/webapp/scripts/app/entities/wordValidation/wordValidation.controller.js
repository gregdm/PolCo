'use strict';

angular.module('polcoApp')
    .controller('WordValidationController', function ($scope, WordValidation, WordValidationOK, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.isInRole = Principal.isInRole;
        $scope.wordValidations = [];
        $scope.loadAll = function() {
            WordValidation.query(function(result) {
               $scope.wordValidations = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            WordValidation.update($scope.wordValidation,
                function () {
                    $scope.loadAll();
                    $('#saveWordValidationModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            WordValidation.get({id: id}, function(result) {
                $scope.wordValidation = result;
                $('#saveWordValidationModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            WordValidation.get({id: id}, function(result) {
                $scope.wordValidation = result;
                $('#deleteWordValidationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            WordValidation.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWordValidationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.validate = function (id) {
            console.log("in validate");
            WordValidationOK.validate(id).then(function(data) {
                    console.log(data + "OK");
                    $scope.loadAll();
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.wordValidation = {value: null, translation: null, wordType: null, number: null, gender: null, person: null, tense: null, id: null};
        };
    });
