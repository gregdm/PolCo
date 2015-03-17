'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pourcentage', {
                parent: 'site',
                url: '/pourcentage',
                data: {
                    roles: [],
                    pageTitle: 'pourcentage.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pourcentage/pourcentage.html',
                        controller: 'PourcentageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pourcentage');
                        return $translate.refresh();
                    }]
                }
            });
    });
